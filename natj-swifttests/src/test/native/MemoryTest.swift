public var isReleased = false

public class DummyObject {

    public init() {
        isReleased = false
    }

    deinit {
        isReleased = true
    }
}

public func getIsReleased() -> Bool {
    return isReleased;
}