/*
 * Copyright 2019 John A. De Goes and the ZIO Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.test

import zio.{ Managed, UIO }

trait TestExecutor[L, -T] {
  def execute(spec: Spec[L, T], defExec: ExecutionStrategy): UIO[ExecutedSpec[L]]
}
object TestExecutor {
  def managed[R, E, L](environment: Managed[E, R]): TestExecutor[L, ZTest[R, E]] =
    new TestExecutor[L, ZTest[R, E]] {
      def execute(spec: ZSpec[R, E, L], defExec: ExecutionStrategy): UIO[ExecutedSpec[L]] =
        spec.foreachExec(defExec) { test =>
          test.provideManaged(environment).foldCause(fail(_), identity(_))
        }
    }
}
