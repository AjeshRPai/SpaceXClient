package com.android.spacexclient.presentation

import com.android.spacexclient.presentation.utils.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler


class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider
{
    fun triggerActions() {
        testScheduler.triggerActions()
    }

    override fun ui(): Scheduler {
        return testScheduler
    }

    override fun computation(): Scheduler {
        return testScheduler
    }

    override fun io(): Scheduler {
        return testScheduler
    }
}
