public struct TestStruct {
    public var field: Int = 5

    public var randomField2: Int = 7

    public init() {

    }

    public func getStructNumber(_ par1: Int) -> Int {
        return randomField2 + par1
    }
}

public func testFunc(_ par1: TestStruct, _ par2: Int) -> Int {
    return par1.field + par2
}