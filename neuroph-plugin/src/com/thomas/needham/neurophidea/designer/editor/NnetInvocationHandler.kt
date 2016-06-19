package com.thomas.needham.neurophidea.designer.editor

import org.jetbrains.annotations.Nullable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Created by thoma on 18/06/2016.
 */
class NnetInvocationHandler : InvocationHandler {
    val documentManager : NnetDocumentManager
    constructor(documentManager : NnetDocumentManager){
        this.documentManager = documentManager
    }
    @Nullable
    @Throws(Throwable::class)
    override fun invoke(proxy : Any?, method : Method?, args : Array<out Any>?) : Any? {
        documentManager.multiCast(method,args)
        return null
    }
}