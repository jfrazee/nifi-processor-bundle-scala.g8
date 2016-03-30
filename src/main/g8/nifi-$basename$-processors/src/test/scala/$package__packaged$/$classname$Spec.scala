package $package$

import java.io._

// ScalaTest
import org.scalatest._

// NiFi
import org.apache.nifi.util.{ TestRunner, TestRunners }

class $classname$Spec extends FunSpec {
  import scala.collection.JavaConverters._

  val SomeContent = "some content"

  describe("$classname$") {
    it("should successfully transfer a FlowFile") {
      val processor = new $classname$
      val runner = TestRunners.newTestRunner(processor)
      runner.setProperty(processor.MY_PROPERTY, "1234")

      val content = new ByteArrayInputStream(SomeContent.getBytes)
      runner.enqueue(content)
      runner.run(1)

      runner.assertTransferCount(processor.REL_SUCCESS, 1)
      runner.assertTransferCount(processor.REL_FAILURE, 0)

      for (flowFile <- runner.getFlowFilesForRelationship(processor.REL_SUCCESS).asScala) {
        flowFile.assertContentEquals(SomeContent)
      }
    }
  }
}
