//
//  SwiftNatJGlobal.swift
//  natj
//
//  Created by berstanio on 17.12.22.
//

import Foundation

public func createCString(_ string: String) -> UnsafeBufferPointer<CChar> {
    
    let cString = string.utf8CString
    let buffer: UnsafeBufferPointer<CChar> = cString.withUnsafeBytes { rawBuffer in
        let copy = UnsafeMutableRawBufferPointer.allocate(
            byteCount: rawBuffer.count,
            alignment: MemoryLayout<CChar>.alignment)
        copy.copyMemory(from: rawBuffer)
        return UnsafeBufferPointer(copy.bindMemory(to: CChar.self))
    }
    
    return buffer
}
