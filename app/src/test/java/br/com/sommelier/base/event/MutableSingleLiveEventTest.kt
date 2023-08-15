package br.com.sommelier.base.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MutableSingleLiveEventTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: MutableSingleLiveEvent<String>

    private lateinit var observer: Observer<String>
    private lateinit var owner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setUp() {
        owner = mockk()
        observer = mockk(relaxed = true)

        // Link custom lifecycle owner with the lifecycle register
        lifecycle = LifecycleRegistry(owner)
        every { owner.lifecycle } returns lifecycle

        // Start live event and observing
        sut = MutableSingleLiveEvent()
        sut.observe(owner, observer)

        // Start in a non-active state
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    @Test
    fun `WHEN no value has been set and lifecycle event is ON_RESUME THEN onChange must not be called`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        verify(inverse = true) { observer.onChanged(any()) }
    }

    @Test
    fun `WHEN the value has been set and lifecycle event passes the second time on OnResume THEN SingleLiveEvent must notify just once`() {
        val expectedValue = "Test 1"
        sut.value = expectedValue

        // Observers are called once on resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // this value must not been set or emitted because the lifecycle is not active
        sut.value = "test2"

        // On second resume, no update should be emitted.
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // Check that the observer is called once
        verify(exactly = 1) { observer.onChanged(expectedValue) }
    }

    @Test
    fun `WHEN the value has been set twice THEN SingleLiveEvent must notify twice`() {
        val expectedValue = "Test 1"
        val expectedValueTwo = "Test 2"
        // set value once
        sut.value = expectedValue

        // Observers are called once on resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // when value is set again, observers are called again
        sut.value = expectedValueTwo

        // verify if is called twice
        verify(exactly = 2) { observer.onChanged(any()) }
    }

    @Test
    fun `WHEN lifecycle is not active THEN SingleLiveEvent must notify just when lifecycle is active`() {
        val firstValue = "FirstValue"
        val secondValue = "SecondValue"
        sut.value = firstValue

        verify(inverse = true) { observer.onChanged(firstValue) }

        sut.value = secondValue

        // Observers are called once on resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        verify(exactly = 1) { observer.onChanged(secondValue) }
    }

    @Test
    fun `GIVEN multiple observers WHEN new value has been set THEN must notify only one observer`() {
        // Start in a non-active state
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        val firstEventInvoke = mockk<() -> Unit>()
        val secondEventInvoke = mockk<() -> Unit>()
        every { firstEventInvoke() } returns Unit
        every { secondEventInvoke() } returns Unit
        sut = MutableSingleLiveEvent()
        sut.observe(owner) { firstEventInvoke() }
        sut.observe(owner) { secondEventInvoke() }

        sut.value = "Test 1"

        // Observers are called once on resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        verify(exactly = 1) { firstEventInvoke() }
        verify(inverse = true) { secondEventInvoke() }
    }
}
