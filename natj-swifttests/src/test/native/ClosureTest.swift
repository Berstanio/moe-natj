public func closurePassTest(_ par1: Int, completion: (Int) -> Int) -> Int {
    return completion(par1 + 5)
}

public func closureReturnTest(_ par1: Int, completion: @escaping (Int) -> Int) -> (Int) -> Int {
    return completion
}