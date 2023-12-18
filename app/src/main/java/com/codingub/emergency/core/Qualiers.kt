package com.codingub.emergency.core

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Local

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Interceptors

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkInterceptors