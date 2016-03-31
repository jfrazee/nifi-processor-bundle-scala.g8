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

// NiFi
import org.apache.nifi.processor.Relationship

trait $classname$Relationships {
  val RelSuccess =
    new Relationship.Builder()
      .name("success")
      .description("""
        Any FlowFile that is successfully transferred is routed to this relationship
      """.trim)
      .build

  val RelFailure =
    new Relationship.Builder()
      .name("failure")
      .description("""
          Any FlowFile that fails to be transferred is routed to this relationship
      """.trim)
      .build

  lazy val relationships = Set(RelSuccess, RelFailure)
}

object $classname$Relationships extends $classname$Relationships {
}
