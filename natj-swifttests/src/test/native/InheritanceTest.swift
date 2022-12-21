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

public class UnknownSubClass : SubClass {

    public override func getClassSpecificNumber() -> Int {
        return 3
    }
}

public func getUnknownSubClassAsBaseClass() -> BaseClass {
    return UnknownSubClass()
}

public func globalGetClassSpecificNumber(_ par1: BaseClass) -> Int {
    return par1.getClassSpecificNumber()
}