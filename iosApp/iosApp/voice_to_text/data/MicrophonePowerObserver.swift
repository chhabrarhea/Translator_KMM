//
//  MicrophonePowerObserver.swift
//  iosApp
//
//  Created by Rhea Chhabra on 15/07/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Speech // SPEECH RECOGNITION
import Combine // REACTIVE PROGRAMMING

class MicrophonePowerObserver: ObservableObject {
    // Reactive DS for Observe something and later cancel, assign it to an instance and then when assigned back to nil the scope is cancelled
    private var cancellable: AnyCancellable? = nil
    
    private var audioRecorder: AVAudioRecorder? = nil
    
    // Something observable with a private setter
    @Published private(set) var micPowerRatio = 0.0
    
    private let powerRatioEmissionsPerSecond = 20.0
    
    func startObserving(){
        do {
            // key value pair
            let recorderSettings: [String: Any] = [
                // lossless recording type
                AVFormatIDKey: NSNumber(value: kAudioFormatAppleLossless),
                AVNumberOfChannelsKey: 1
            ]
            /*
             url: where to save the resultant file, since we do not care about storing the file we are sending it into a void
             if exception is thrown in try, it will be caught in catch of do
             */
            let recorder = try AVAudioRecorder(url: URL(fileURLWithPath: "/dev/null", isDirectory: true), settings: recorderSettings)
            // metering of input audio levels
            recorder.isMeteringEnabled = true
            recorder.record()
            self.audioRecorder = recorder
            
            self.cancellable = Timer.publish(
                every: 1.0/powerRatioEmissionsPerSecond,
                tolerance: 1.0 / powerRatioEmissionsPerSecond,
                on: .main,
                in: .common
            )
            .autoconnect()
            /*
             like collect in flow
             executed in another thread, therefore it is important to cancel them
             if the Object Instance is destroyed before cancellation occurs, this will lead to memory leak since self instance cannot be destroyed
             weak self allows the self instance to be set to null in such case & is a good practice
             */
            .sink { [weak self]_ in
                recorder.updateMeters()
                let powerOffset = recorder.averagePower(forChannel: 0) // we have only 1 channel in our audio recorder
                if powerOffset < -50 {
                    self?.micPowerRatio = 0.0
                } else {
                    self?.micPowerRatio = CGFloat(50 + powerOffset) / 50 // normalized value
                }
            }
        } catch {
            print("Error occured when observing microphone power: \(error.localizedDescription)")
        }
    }
    
    func release() {
        cancellable = nil
        audioRecorder?.stop()
        audioRecorder = nil
        micPowerRatio = 0.0
    }
}
