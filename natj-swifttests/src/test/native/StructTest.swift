public struct TestStruct {
    public var field: Int = 5

    public var randomField2: Int = 7

    public init() {

    }

    public func getStructNumber(_ par1: Int) -> Int {
        return randomField2 + par1
    }

    public func getStructNumber2() -> Int {
        return randomField2
    }
}

public struct TestMediumStruct {
    public var field1: Int = 1
    public var field2: Int = 2
    public var field3: Int = 3

    public init() {

    }

    public func getStructNumber(_ par1: Int) -> Int {
        return field3 + par1
    }

    public func getStructNumber2() -> Int {
        return field3
    }
}

public struct TestBigStruct {
    public var field1: Int = 1
    public var field2: Int = 2
    public var field3: Int = 3
    public var field4: Int = 4
    public var field5: Int = 5

    public init() {

    }

    public func getStructNumber(_ par1: Int) -> Int {
        return field4 + par1
    }

    public func getStructNumber2() -> Int {
        return field4
    }
}

public func testFuncBig(_ par1: TestBigStruct, _ par2: Int) -> Int {
    return par1.field3 + par2
}

public func testFuncMedium(_ par1: TestBigStruct, _ par2: Int) -> Int {
    return par1.field4 + par2
}

public func testFunc(_ par1: TestStruct, _ par2: Int) -> Int {
    return par1.field + par2
}