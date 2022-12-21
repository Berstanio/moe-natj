public protocol TestProtocol {
    func protoFunc() -> Int;
}

public class ClassConformingProtocol : TestProtocol {

    public func protoFunc() -> Int {
        return 3
    }

    public init() {

    }
}

public class UnknownClassConformingProtocol : TestProtocol {
    public func protoFunc() -> Int {
        return 5
    }
}

public struct StructConformingProtocol : TestProtocol {

    public var fieeeld: Int = 10
    public var fieeeld2: Int = 11
    public var fieeeld3: Int = 12
    public var fieeeld4: Int = 13

    public func protoFunc() -> Int {
        return 2
    }
}

public struct SmallStructConformingProtocol : TestProtocol {

    public var fieeeld: Int = 10
    public var fieeeld2: Int = 11

    public func protoFunc() -> Int {
        return 4
    }
}

public func getClassAsProtocol() -> TestProtocol {
    return ClassConformingProtocol()
}

public func getStructAsProtocol() -> TestProtocol {
    return StructConformingProtocol()
}

public func getUnknownClassAsProtocol() -> TestProtocol {
    return UnknownClassConformingProtocol()
}

public func passBackUnknownProtocol(_ par1: TestProtocol) -> Int {
    return par1.protoFunc()
}

public func getSmallStructAsProtocol() -> TestProtocol {
    return SmallStructConformingProtocol()
}