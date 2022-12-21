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

public struct StructConformingProtocol : TestProtocol {

    public var fieeeld: Int = 10
    public var fieeeld2: Int = 11
    public var fieeeld3: Int = 12
    public var fieeeld4: Int = 13

    public func protoFunc() -> Int {
        return 2
    }
}

public func getClassAsProtocol() -> TestProtocol {
    return ClassConformingProtocol()
}

public func getStructAsProtocol() -> TestProtocol {
    return StructConformingProtocol()
}