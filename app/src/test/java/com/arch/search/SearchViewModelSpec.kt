package com.arch.search

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.arch.search.di.KoinSpek
import com.arch.search.di.test_module
import com.arch.search.viewmodel.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.inject
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.test.assertEquals

object SearchViewModelSpec : KoinSpek({

    beforeEachTest {
        startKoin {
            modules(test_module)
        }
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    afterEachTest {
        ArchTaskExecutor.getInstance().setDelegate(null)
        stopKoin()
    }

    val viewModel: SearchViewModel by inject()

    Feature("SearchViewModel spec") {

        Scenario("빈값이 아닌,검색어를  입력 받는다") {
            val search = "android"

            When("${search}을 입력 받았을 때 ") {
                viewModel.searchImages(search)
                Thread.sleep(100)
            }

            Then("검색 개수는 10개 ") {
                assertEquals(10, viewModel.showSearchCount.value)
            }
            Then("리스트 개수는  10개 ") {
                val searchs = viewModel.adapter.value?.mDataList?:ArrayList()
                assertEquals(10,searchs.size)
            }

            Then("5번째 login 은 android_4") {
                val searchs = viewModel.adapter.value?.mDataList?:ArrayList()
                if(searchs.size ==10){
                    assertEquals("android_4",searchs[4].id)
                }

            }
        }

        Scenario("empty 입력 받는다(빈 리스트 테스트)") {
            val name = "empty"

            When("empty  이름을 입력 받았을 때") {
                viewModel.searchImages(name)
                Thread.sleep(100)
            }
            Then("검색 개수는 0개 ") {
                assertEquals(0, viewModel.showSearchCount.value)
            }
            Then("리스트 개수는  0개 ") {
                val books = viewModel.adapter.value?.mDataList?:ArrayList()
                assertEquals(0,books.size)
            }
        }

        Scenario("공백  이름을 입력 받는다(통신 에러 상황)") {

            When("공백 이름을 입력 받았을 때") {
                viewModel.searchImages("")
                Thread.sleep(100)
            }
            Then("검색 개수는 0개 ") {
                assertEquals(0, viewModel.showSearchCount.value)
            }
            Then("리스트 개수는  0개 ") {
                val books = viewModel.adapter.value?.mDataList?:ArrayList()
                assertEquals(0,books.size)
            }
        }
    }
})


fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
