/**
 *  Copyright 2003-2010 Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sf.ehcache.config.generator.xsom;

import net.sf.ehcache.config.generator.model.NodeAttribute;
import net.sf.ehcache.config.generator.model.NodeElement;

public interface XSDAttributeValueFactory {

    public static final XSDAttributeValueFactory DEFAULT_FACTORY = new XSDAttributeValueFactory() {

        public String createValueForAttribute(NodeElement element, NodeAttribute attribute, XSDAttributeValueType xsdAttributeValueType) {
            return xsdAttributeValueType.getRandomAllowedValue();
        }
    };

    public String createValueForAttribute(NodeElement element, NodeAttribute attribute, XSDAttributeValueType xsdAttributeValueType);
}
