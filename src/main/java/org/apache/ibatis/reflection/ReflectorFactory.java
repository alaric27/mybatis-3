/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection;

/**
 * 主要是对Reflector的创建和缓存
 */
public interface ReflectorFactory {

  /**
   * 检测该 ReflectorFactory 对象是否会缓存 Reflector 对象
   * @return
   */
  boolean isClassCacheEnabled();

  /**
   * 设置是否缓存 Reflector 对象
   * @param classCacheEnabled
   */
  void setClassCacheEnabled(boolean classCacheEnabled);

  Reflector findForClass(Class<?> type);
}