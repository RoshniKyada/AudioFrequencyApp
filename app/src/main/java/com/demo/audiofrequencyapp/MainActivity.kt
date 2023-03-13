package com.demo.audiofrequencyapp

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.demo.audiofrequencyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding  // Defining the binding class for the activity_main.xml layout
    private lateinit var audioTrack: AudioTrack  // Defining the AudioTrack object to play sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewControls() // Initializing the view controls of the activity
    }

    private fun initViewControls() {
        binding.buttonPlay.setOnClickListener {
            playAudio()
        }
        binding.editTextFrequency.addTextChangedListener {
            it.toString().toIntOrNull()?.let { value ->
                if (value < 1 || value > 24000) { // Check if the entered frequency is within the range of 1 to 24000 Hz
                    binding.buttonPlay.isEnabled = false
                    binding.editTextFrequency.error = getString(R.string.audio_frequency_range_input_error)
                } else {
                    binding.buttonPlay.isEnabled = true
                    binding.editTextFrequency.error = null
                }
            } ?: kotlin.run { // Show an error message to the user if the entered text is not a valid integer
                binding.buttonPlay.isEnabled = false //
                binding.editTextFrequency.error = getString(R.string.audio_frequency_range_input_error)
            }
        }
    }

    private fun playAudio() {
        val frequency = binding.editTextFrequency.text.toString().toInt() // Get the frequency entered by the user
        val duration = 500 // Set the duration to 500 ms

        val sampleRate = 44100 // Set the sample rate

        // Calculate the minimum buffer size required to play the audio
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val audioAttributes = AudioAttributes.Builder().build() // Build audio attributes for the AudioTrack object

        // Build audio format for the AudioTrack object
        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .build()

        audioTrack = AudioTrack(audioAttributes, audioFormat, bufferSize, AudioTrack.MODE_STREAM, 0) // Initialize the AudioTrack object with the specified attributes, format, and buffer size

        val numSamples = (duration * sampleRate) / 1000 //calculate number of samples
        val buffer = ShortArray(numSamples) //create buffer array to store audio samples

        for (i in 0 until numSamples) { //loop through each sample
            val sample =
                (Math.sin(2.0 * Math.PI * i.toDouble() / (sampleRate / frequency)) * 32767).toInt()
                    .toShort() //generate audio sample for given frequency and store in buffer
            buffer[i] = sample //store audio sample in buffer
        }

        audioTrack.write(buffer, 0, numSamples)  // Write the buffer to the AudioTrack object.
        audioTrack.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioTrack.stop() //stop audio track
        audioTrack.release() //release audio track resources
    }
}