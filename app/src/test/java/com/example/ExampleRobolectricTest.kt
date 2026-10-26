package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.SoulSyncRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SoulSync", appName)
  }

  @Test
  fun `verify repository initial state and messaging`() {
    val repo = SoulSyncRepository()
    assertTrue(repo.messages.value.isNotEmpty())
    val initialSize = repo.messages.value.size

    repo.sendMessage("I love you to the moon and back!", senderId = "user", senderName = "Taylor")
    assertEquals(initialSize + 1, repo.messages.value.size)
    assertEquals("I love you to the moon and back!", repo.messages.value.last().text)
  }

  @Test
  fun `verify daily spark prompt answering`() {
    val repo = SoulSyncRepository()
    repo.answerDailyPrompt("Your radiant smile in the morning")
    assertEquals("Your radiant smile in the morning", repo.dailyPrompt.value.userAnswer)
    assertTrue(repo.dailyPrompt.value.isAnsweredByUser)
  }
}
