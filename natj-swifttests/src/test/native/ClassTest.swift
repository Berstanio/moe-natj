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

public func test2(_ par1: Vehicle, _ par2: Int) {
    print("My speed is: \(par1.currentSpeed) and par \(par2)")
}



public func pleeeaseWork() -> Vehicle {
    print("OMG I WORKED")
    let v: Vehicle = Vehicle()
    v.currentSpeed = 100
    print(Unmanaged<Vehicle>.passRetained(v).toOpaque())
    return v
}

public func funcWithMoreParam(par1: Int, par2: Int) {
    print("Got called global \(par1) and \(par2)")
}
//print(test(1).currentSpeed)
//print(MemoryLayout<Vehicle>.size)
//let tes = Vehicle()
//let tes2 = Vehicle()
//print(type(of: tes) == type(of: tes2))