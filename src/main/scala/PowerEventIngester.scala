import java.util.LinkedList;
import io.prediction.EventClient;
import org.joda.time.DateTime;
import collection.JavaConversions._


import io.prediction.Event;
import scala.io.Source

object PowerEventIngester{
  
  
  def main(args:Array[String]) {
    if(args.size != 2){
	println("Usage: PowerEventIngester <pio app accesskey> <input file of power plant data>")
	sys.exit(0)
    }
    val accessKey = args(0)
    val fileName = args(1)
    val client = new EventClient(accessKey);
    
    var i = 0
    val eventObjects = for(line <- Source.fromFile(fileName).getLines()) yield {
      
      val tokens  = line.split(",")
      val properties = new java.util.HashMap[String, Object]()
      properties += "AT" -> tokens(0)
      properties += "V"  -> tokens(1)
      properties += "AP" -> tokens(2)
      properties += "RH" -> tokens(3)
      properties += "PE" -> tokens(4)

      val event = new Event()
        .event("$set")
        .entityType("iot")
        .entityId(i.toString())
        .properties(properties)
        .eventTime(new DateTime())
      i += 1
      event
    }
    

    
    for((group, index) <- eventObjects.toList.grouped(50).zipWithIndex){
      val events = new LinkedList[Event]();
      group.foreach(e => events.add(e))
      client.createEvents(events);
      println(s"Batch: $index, Size of batch: ${group.size}")
    }
    client.close()
  }
}

