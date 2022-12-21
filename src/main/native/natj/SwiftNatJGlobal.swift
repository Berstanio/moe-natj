//
//  SwiftNatJGlobal.swift
//  natj
//
//  Created by berstanio on 17.12.22.
//

import Foundation

public func createCString(_ string: String) -> UnsafeMutablePointer<CChar> {
    var cPointer = UnsafeMutablePointer<CChar>(mutating: string.cString(using: .utf8))

    return cPointer!
}
