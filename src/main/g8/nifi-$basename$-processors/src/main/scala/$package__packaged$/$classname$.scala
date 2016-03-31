/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package $package$

import java.io._
import java.util.concurrent.atomic.AtomicReference

// Commons IO
import org.apache.commons.io.IOUtils

// NiFi
import org.apache.nifi.flowfile.FlowFile
import org.apache.nifi.components.PropertyDescriptor
import org.apache.nifi.processor.io.InputStreamCallback
import org.apache.nifi.processor.{ AbstractProcessor, Relationship }
import org.apache.nifi.processor.{ ProcessorInitializationContext, ProcessContext, ProcessSession }
import org.apache.nifi.annotation.behavior.{ ReadsAttribute, ReadsAttributes }
import org.apache.nifi.annotation.behavior.{ WritesAttribute, WritesAttributes }
import org.apache.nifi.annotation.documentation.{ CapabilityDescription, SeeAlso, Tags }
import org.apache.nifi.annotation.lifecycle.OnScheduled

// Typesafe Config
import com.typesafe.config.ConfigFactory

@Tags(Array("example"))
@CapabilityDescription("An example processor")
@SeeAlso(Array())
@ReadsAttributes(Array(
  new ReadsAttribute(attribute="", description=""))
)
@WritesAttributes(Array(
  new WritesAttribute(attribute="", description=""))
)
class $classname$ extends AbstractProcessor with $classname$Properties
  with $classname$Relationships {
    
  import scala.collection.JavaConverters._

  private[this] val className = this.getClass.getName

  private[this] lazy val config = ConfigFactory.load().getConfig(className)

  protected[this] override def init(context: ProcessorInitializationContext): Unit = {
  }

  override def getSupportedPropertyDescriptors(): java.util.List[PropertyDescriptor] = {
    properties.asJava
  }

  override def getRelationships(): java.util.Set[Relationship] = {
    relationships.asJava
  }

  @OnScheduled
  def onScheduled(context: ProcessContext): Unit = {
  }

  override def onTrigger(context: ProcessContext, session: ProcessSession): Unit = {
    val flowFile = session.get

    Option(flowFile) match {
      case Some(f) => {
        val property =
          context.getProperty(ExampleProperty)
            .evaluateAttributeExpressions(flowFile)
            .getValue

        val content = new AtomicReference[String]
        session.read(flowFile, new InputStreamCallback {
          override def process(in: InputStream): Unit = {
              try {
                val s = IOUtils.toString(in)
                content.set(s)
              }
              catch {
                case t: Throwable =>
                  getLogger().error(t.getMessage, t)
                  session.transfer(flowFile, RelFailure)
              }
            }
        })
      }
      case _ =>
        getLogger().warn("FlowFile was null")
    }

    session.transfer(flowFile, RelSuccess)
  }
}
