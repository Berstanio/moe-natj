public enum TestEnum {
    case EmptyTestCase
    case NotEmptyTestCase(Int)
    case SecondTestCase(Int)
    case FloatTestCase(Float)

    public func testEnumFunction(_ par1: Int) -> Int {
        var toAdd = 0
        switch self {
            case .EmptyTestCase:
                toAdd = 1
            case .NotEmptyTestCase(let n):
                toAdd = n
            case .SecondTestCase(let n):
                toAdd = n
            case .FloatTestCase(let n):
                toAdd = Int(n)
        }

        return toAdd + par1
    }
}

public enum LargeTestEnum {
    case EmptyTestCase
    case NotEmptyTestCase(Int)
    case SecondTestCase(Int, Int)
    case LargeTestCase(Int, Int, Int, Int, Int)

    public func testEnumFunction(_ par1: Int) -> Int {
        var toAdd = 0
        switch self {
            case .EmptyTestCase:
                toAdd = 1
            case .NotEmptyTestCase(let n):
                toAdd = n
            case .SecondTestCase(let n1, let n2):
                toAdd = n1 + n2
            case .LargeTestCase(let n1, let n2, let n3, let n4, let n5):
                toAdd = n1 + n2 + n3 + n4 + n5
        }

        return toAdd + par1
    }
}

public enum RawTestEnum {
    case test1, test2, test3, test4
}

public func returnEnum() -> TestEnum {
    return TestEnum.NotEmptyTestCase(19)
}

public func returnEnumLarge() -> LargeTestEnum {
    return LargeTestEnum.SecondTestCase(19, 7)
}

public func retrieveEnumLarge(_ par1: LargeTestEnum) -> Int {
    switch par1 {
        case .EmptyTestCase:
            return 7
        case .NotEmptyTestCase(let n):
            return n
        case .SecondTestCase(let n1, let n2):
            return n1 + n2
        case .LargeTestCase(let n1, let n2, let n3, let n4, let n5):
            return n1 + n2 + n3 + n4 + n5
    }
    return -1
}

public func retrieveEnum(_ par1: TestEnum) -> Int {
    switch par1 {
        case .EmptyTestCase:
            return 7
        case .NotEmptyTestCase(let n):
            return n
        case .SecondTestCase(let n):
            return n + 1
        case .FloatTestCase(let n):
            return Int(n)
    }
    return -1
}