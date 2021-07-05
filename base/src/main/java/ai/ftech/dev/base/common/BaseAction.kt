package ai.ftech.dev.base.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

abstract class BaseAction<P : BaseAction.RequestValue, R> {

    open val dispatcher: CoroutineDispatcher = Dispatchers.IO

    interface RequestValue

    class VoidRequest : RequestValue

    protected abstract suspend fun execute(requestValue: P): R

    suspend fun invoke(requestValue: P): Flow<R> {
        return flow {

            val response = withContext(dispatcher) {

                execute(requestValue)

            }

            emit(response)
        }.flowOn(dispatcher)
    }
}