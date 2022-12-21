public class Vehicle {
    // NOTE: Double uses d0 register, libffi doesn't do that correctly?
    public var currentSpeed: Int = 0
    public var description: String {
        return "traveling at \(currentSpeed) miles per hour"
    }
    public init() {

    }

    deinit {
        print("DE-INIT")
    }
    public func currentSpeedPlusPar(_ par1: Int) -> Int {
        return currentSpeed + par1
    }
}