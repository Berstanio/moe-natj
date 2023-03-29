public class SubscriptClass {
    public var subscriptValue: Int = 1
    
    public subscript(index: Int) -> Int {
        get {
            return index * subscriptValue
        }
        set(newValue) {
            subscriptValue = newValue * index
        }
    }

    public init() {

    }
}

public struct SubscriptStruct {
    public var subscriptValue: Int = 1
    
    public subscript(index: Int) -> Int {
        get {
            return index * subscriptValue
        }
        set(newValue) {
            subscriptValue = newValue * index
        }
    }
    public init() {

    }
}