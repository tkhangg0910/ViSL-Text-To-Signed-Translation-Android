package com.example.final_project.data.repository
import com.example.final_project.domain.model.ReplayState
import javax.inject.Inject
import javax.inject.Singleton
interface ReplayRepository {
    fun setReplay(state: ReplayState)
    fun consumeReplay(): ReplayState?

}


@Singleton
class ReplayRepositoryImpl @Inject constructor() : ReplayRepository {
    private var pending: ReplayState? = null
    override fun setReplay(state: ReplayState) { pending = state }
    override fun consumeReplay(): ReplayState? = pending?.also { pending = null }
}