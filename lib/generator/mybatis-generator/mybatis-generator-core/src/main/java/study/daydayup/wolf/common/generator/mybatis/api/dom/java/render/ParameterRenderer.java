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
package study.daydayup.wolf.common.generator.mybatis.api.dom.java.render;

import study.daydayup.wolf.common.generator.mybatis.api.dom.java.CompilationUnit;
import study.daydayup.wolf.common.generator.mybatis.api.dom.java.JavaDomUtils;
import study.daydayup.wolf.common.generator.mybatis.api.dom.java.Parameter;
import study.daydayup.wolf.common.generator.mybatis.internal.util.CustomCollectors;

public class ParameterRenderer {

    public String render(Parameter parameter, CompilationUnit compilationUnit) {
        return renderAnnotations(parameter)
                + JavaDomUtils.calculateTypeName(compilationUnit, parameter.getType())
                + " " //$NON-NLS-1$
                + (parameter.isVarargs() ? "... " : "") //$NON-NLS-1$ //$NON-NLS-2$
                + parameter.getName();
    }
    
    // should return empty string if no annotations
    private String renderAnnotations(Parameter parameter) {
        return parameter.getAnnotations().stream()
                .collect(CustomCollectors.joining(" ", "", " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}