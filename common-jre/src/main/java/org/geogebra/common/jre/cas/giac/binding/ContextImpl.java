package org.geogebra.common.jre.cas.giac.binding;

import org.geogebra.common.cas.giac.binding.Context;

import javagiac.context;

class ContextImpl implements Context {

    context context;

    ContextImpl() {
        context = new context();
    }
}
