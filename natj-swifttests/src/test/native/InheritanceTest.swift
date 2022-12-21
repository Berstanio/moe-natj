public class BaseClass {
    public func getClassSpecificNumber() -> Int {
        return 1
    }

    public var baseField: Int = 6

    public func getBaseField() -> Int {
        return baseField
    }

    public init() {

    }
}

public class SubClass : BaseClass {

    public var anotherField: Int = 5

    public func onlySubClass() -> Int {
        return anotherField
    }

    public override func getClassSpecificNumber() -> Int {
        return 2
    }

    public static func getSubClassAsBaseClass() -> BaseClass {
        return SubClass()
    }
}
