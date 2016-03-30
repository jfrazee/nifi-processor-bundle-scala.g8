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
import javax.net.ssl.SSLContext
import java.util.concurrent.atomic.AtomicReference

// Commons IO
import org.apache.commons.io.IOUtils

// NiFi
import org.apache.nifi.flowfile.FlowFile
import org.apache.nifi.ssl.SSLContextService
import org.apache.nifi.ssl.SSLContextService.ClientAuth
import org.apache.nifi.processor.io.InputStreamCallback
import org.apache.nifi.processor.util.StandardValidators
import org.apache.nifi.processor.{ AbstractProcessor, Relationship }
import org.apache.nifi.processor.{ ProcessorInitializationContext, ProcessContext, ProcessSession }
import org.apache.nifi.components.{ PropertyDescriptor, PropertyValue }
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
class $classname$ extends AbstractProcessor {
  import scala.collection.JavaConverters._

  private[this] val className = this.getClass.getName

  private[this] val config = ConfigFactory.load().getConfig(className)

  val MY_PROPERTY =
    new PropertyDescriptor.Builder()
      .name("My Property")
      .description("Whatever my property does")
      .required(true)
      .expressionLanguageSupported(true)
      .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
      .build

  val SSL_CONTEXT_SERVICE =
    new PropertyDescriptor.Builder()
      .name("SSL Context Service")
      .description("""
        The SSL Context Service used to provide client certificate information for TLS/SSL enabled site-to-site connections.
      """.trim)
      .required(false)
      .identifiesControllerService(classOf[SSLContextService])
      .build

  val CLIENT_AUTH =
    new PropertyDescriptor.Builder()
      .name("Client Auth")
      .description("""
        The client authentication policy to use for the SSL Context. Only used if an SSL Context Service is provided.
      """.trim)
      .required(false)
      .allowableValues(ClientAuth.values)
      .defaultValue(ClientAuth.REQUIRED.name)
      .build

  val REL_SUCCESS =
    new Relationship.Builder()
      .name("success")
      .description("""
        Any FlowFile that is successfully transferred is routed to this relationship
      """.trim)
      .build

  val REL_FAILURE =
    new Relationship.Builder()
      .name("failure")
      .description("""
          Any FlowFile that fails to be transferred is routed to this relationship
      """.trim)
      .build

  private[this] var properties: List[PropertyDescriptor] = List.empty[PropertyDescriptor]

  private[this] var relationships: Set[Relationship] = Set.empty[Relationship]

  protected[this] override def init(context: ProcessorInitializationContext): Unit = {
    properties = List(MY_PROPERTY, SSL_CONTEXT_SERVICE, CLIENT_AUTH)
    relationships = Set(REL_SUCCESS)
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
        val myProperty =
          context.getProperty(MY_PROPERTY)
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
                  session.transfer(flowFile, REL_FAILURE)
              }
            }
        })
      }
      case _ =>
        getLogger().warn("FlowFile was null")
    }

    session.transfer(flowFile, REL_SUCCESS)
  }
}
