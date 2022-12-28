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

    public var fff: Int = 7
    public func protoFunc() -> Int {
        return fff
    }
}

public struct StructConformingProtocol : TestProtocol {

    public var fieeeld: Int = 10
    public var fieeeld2: Int = 11
    public var fieeeld3: Int = 12
    public var fieeeld4: Int = 2
    public var fieeeld5: Int = 2

    public func protoFunc() -> Int {
        return fieeeld4
    }
}

public struct SmallStructConformingProtocol : TestProtocol {

    public var fieeeld: Int = 10
    public var fieeeld2: Int = 11

    public func protoFunc() -> Int {
        return fieeeld2
    }
}

public enum TestEnumConformingProtocol : TestProtocol {
    case EmptyTestCase
    case NotEmptyTestCase(Int)
    case SecondTestCase(Int)
    case FloatTestCase(Float)

    public func protoFunc() -> Int {
        switch self {
            case .EmptyTestCase:
                return 1
            case .NotEmptyTestCase(let n):
                return n
            case .SecondTestCase(let n):
                return n
            case .FloatTestCase(let n):
                return Int(n)
        }
    }
}

public enum LargeTestEnumConformingProtocol : TestProtocol {
    case EmptyTestCase
    case NotEmptyTestCase(Int)
    case SecondTestCase(Int, Int)
    case LargeTestCase(Int, Int, Int, Int, Int)

    public func protoFunc() -> Int {
        switch self {
            case .EmptyTestCase:
                return 1
            case .NotEmptyTestCase(let n):
                return n
            case .SecondTestCase(let n1, let n2):
                return n1 + n2
            case .LargeTestCase(let n1, let n2, let n3, let n4, let n5):
                return n1 + n2 + n3 + n4 + n5
        }
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

public func getTestEnumAsProtocol() -> TestProtocol {
    return TestEnumConformingProtocol.SecondTestCase(15)
}

public func getLargeTestEnumAsProtocol() -> TestProtocol {
    return LargeTestEnumConformingProtocol.SecondTestCase(15, 20)
}