package chart

interface Type {
    companion object {
        inline val line: Type get() = Type("line")
        inline val bar: Type get() = Type("bar")

        inline val horizontalBar: Type get() = Type("horizontalBar")
        inline val radar: Type get() = Type("radar")
        inline val doughnut: Type get() = Type("doughnut")
        inline val polarArea: Type get() = Type("polarArea")
        inline val bubble: Type get() = Type("bubble")
        inline val pie: Type get() = Type("pie")
        inline val scatter: Type get() = Type("scatter")
    }
}

inline fun Type(value: String) = value.unsafeCast<Type>()