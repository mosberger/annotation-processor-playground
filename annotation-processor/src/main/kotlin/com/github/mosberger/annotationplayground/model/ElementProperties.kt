package com.github.mosberger.annotationplayground.model

/**
 * Created by domi on 07.02.16.
 */
data class ElementProperties(
        val packageName: String
        , val className: String
        , val layout: Int
        , val viewModels: Iterable<ViewModelProperties>
        , val bindings: Iterable<DataBindingProperties>
)
