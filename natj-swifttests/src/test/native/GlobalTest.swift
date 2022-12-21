public func intSum(par1: Int, par2: Int) -> Int {
    return par1 + par2;
}

public func doubleSum(par1: Double, par2: Double) -> Double {
    return par1 + par2;
}

public func floatSum(par1: Float, par2: Float) -> Float {
    return par1 + par2;
}

public func charTest(_ par1: Character) -> Character {
    if (par1 == "a") {
        return "b"
    } else {
        return "a"
    }
}

public func boolTest(_ par1: Bool) -> Bool {
    return !par1
}

public func stringTest(_ par1: String) -> String {
    return par1 + "!"
}