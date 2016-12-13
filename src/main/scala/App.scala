package s4lClient

import org.mongodb.scala.{MongoClient, Observer, Document}
import org.mongodb.scala.bson.BsonValue
import uk.gov.hmrc.crypto.json.JsonDecryptor
import uk.gov.hmrc.crypto.{CompositeSymmetricCrypto, Crypted}


class ResultObserver(val key : String) extends Observer[Document] {
    def onComplete() : Unit = {
        println("Read Complete")
    }

    def onError(e: Throwable): Unit = {
        println(e)
    }
    def onNext(result: Document): Unit = {
        val doc = Document(result("data").asDocument())
        doc.foreach { tup:(String, BsonValue) => {

            println(tup._1)
            println("====================")
            println(
                CompositeSymmetricCrypto.aes(key, Seq.empty[String])
                    .decrypt(Crypted(tup._2.asString().getValue()))
                    .value)

            println()
        }}
    }
}

object App {
    def main(args : Array[String]) {
        if (args.size != 2 || args(0) != "-k") throw new Exception("Invalid args")

        val resultObserver = new ResultObserver(args(1))

        val mongoClient = MongoClient("mongodb://localhost")
        val db = mongoClient.getDatabase("save4later")
        println(s"connected to ${db.name}")
        
        val collection = db.getCollection("amls-frontend")
        println(s"retrieving collection ${collection.namespace}")

        val results = collection.find().subscribe(resultObserver)

        Thread.sleep(2000)
        mongoClient.close()
    }
}