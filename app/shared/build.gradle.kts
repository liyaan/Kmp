import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser {
            commonWebpackConfig {
                output?.library = null // 不导出全局对象，只导出必要的入口函数
                devtool = "source-map" // 不使用默认的 eval 执行方式构建出 source-map，而是构建单独的 sourceMap 文件
            }
        }
        binaries.executable() //将kotlin.js与kotlin代码打包成一份可直接运行的js文件

    }
    
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//
//    }
    
    android {
       namespace = "com.liyaan.kmp.app.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)
            implementation(libs.coil.network.okhttp)

        }
        commonMain.dependencies {
            api(project(":core"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            //底部导航
            implementation(libs.navigation.compose)

            //ktor 网络请求
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.kotlinx.serialization.json)
            //图片加载
//            implementation("androidx.compose.foundation:foundation:1.12.1")
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
            implementation(libs.ktor.client.js)
            implementation(libs.coil.network.ktor3)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.coil.network.ktor3)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.coil.network.okhttp)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}