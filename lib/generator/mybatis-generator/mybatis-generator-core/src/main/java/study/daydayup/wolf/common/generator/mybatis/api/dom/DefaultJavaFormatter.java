/**
 *    Copyright 2006-2018 the original author or authors.
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
package study.daydayup.wolf.common.generator.mybatis.api.dom;

import study.daydayup.wolf.common.generator.mybatis.api.JavaFormatter;
import study.daydayup.wolf.common.generator.mybatis.api.dom.java.*;
import study.daydayup.wolf.common.generator.mybatis.api.dom.java.render.TopLevelClassRenderer;
import study.daydayup.wolf.common.generator.mybatis.api.dom.java.render.TopLevelEnumerationRenderer;
import study.daydayup.wolf.common.generator.mybatis.api.dom.java.render.TopLevelInterfaceRenderer;
import study.daydayup.wolf.common.generator.mybatis.config.Context;

/**
 * This class is the default formatter for generated Java.  This class will use the
 * built in DOM renderers.
 * 
 * @author Jeff Butler
 *
 */
public class DefaultJavaFormatter implements JavaFormatter, CompilationUnitVisitor<String> {
    protected Context context;

    @Override
    public String getFormattedContent(CompilationUnit compilationUnit) {
        return compilationUnit.accept(this);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String visit(TopLevelClass topLevelClass) {
        return new TopLevelClassRenderer().render(topLevelClass);
    }

    @Override
    public String visit(TopLevelEnumeration topLevelEnumeration) {
        return new TopLevelEnumerationRenderer().render(topLevelEnumeration);
    }

    @Override
    public String visit(Interface topLevelInterface) {
        return new TopLevelInterfaceRenderer().render(topLevelInterface);
    }
}