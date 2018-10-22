package com.gnz.locamat.data


sealed class ResourceState

object Loading : ResourceState()
object EmptyState : ResourceState()
object PopulateState : ResourceState()
object NoLocationGranted : ResourceState()
object LocationError: ResourceState()
data class ErrorState(val throwable: Throwable) : ResourceState()