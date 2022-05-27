# [Sample] Code for Readability

### 구현해야하는 스펙

1. 랜덤으로 생성된 텍스트/색상을 표시하는 리스트를 만든다
2. 항목 선택시, 해당 항목에 `⭑` 표시가 되어야한다
3. 마지막으로 선택된 항목이 존재하는 경우에는 아래 조건을 대응해야한다
   1. 초기화 후 새로운 항목 노출시에도 마지막 선택된 값과 동일한 경우 `2번` 조건을 대응해야한다.

------

<img src="arts/preview.png" width="50%" />

## Step1 : Remove Always run task

![첫번째 시도](arts/step1_first_run_task.png)
![두번째 시도](arts/step1_second_run_task.png)

`build.gradle`에 `buildField`의 값이 **매번 계산해야 되는** 값으로 설정이 되어있었다.
System 타임을 불러오도록 되어있었는데, 이 값을 정적인 값으로 (123) 바꾸고 다시 Build 하면 아래와 같은 결과가 나온다.

![수정 후](arts/step1_after_repair_run_task.png)

<details>
<summary> > 학습한 내용 </summary>

### Gradle Build output
Gradle 이 작업을 실행할 때 콘솔 UI 와 Tooling API를 통해 다른 결과로 작업에 레이블을 저장할 수 있다.
이러한 레이블은 작업에 실행할 작업이 있는지, 해당 작업을 실행해야 하는지, 해당 작업을 실행해야 하는지, 해당 작업을 실행했는지,
해당 작업이 변경되었는지 여부를 기반으로 한다.

#### `(no label)` or `EXECUTED`
Task가 해당 작업을 실행했다.
- Task에는 Action이 있고 Gradle은 빌드의 일부로 실행되어야 한다고 결정하였다.
- Task는 작업과 일부 종속성이 없으며 종속성이 모두 실행된다.

#### `UP-TO-DATE`
Task의 출력은 변경되지 않았다.
- Task에는 출력과 입력이 있으며 변경되지 않았다.
- Task에는 Action이 있지만 Gradle에 출력을 변경하지 않았다고 알려준다.
- Task에는 Action과 일부 종속성이 없지만 모든 종속성은 최신 상태이며 건너뛰거나 캐시에서 가져온다.
- Task에는 Action과 종속성이 없다.

#### `FROM-CACHE`
Task의 출력은 이전 실행에서 찾을 수 있다.
- Task에 빌드 캐시에서 복원된 출력이 있다.

#### `SKIPPED`
Task가 해당 작업을 실행하지 않았다.
- Task가 명령줄에서 명시적으로 제외 되었다.
- 작업에 `OnlyIf` 조건자가 `false`를 반환한다.

#### `NO-SOURCE`
Task는 해당 Action을 실행할 필요가 없다.
- Task에는 입력과 출력이 있지만 소스는 없다. 예를 들어, 소스 파일은 JavaCompile용 .java 파일이다.

[참고 자료 - Gradle page](https://docs.gradle.org/current/userguide/more_about_tasks.html)

</details>

## Step2 : Unnecessary, gradle options

1) `multiDexEnabled true`

min 21에서 불필요한 옵션이다. <br>
[공식문서](https://developer.android.com/studio/build/multidex#mdex-on-l)에 따르면
Android 5.0 이상에서는 **멀티 덱스** 를 지원한다고 명시되어 있다.
따라서 삭제를 해준다.

2) `vectorDrawables.useSupportLibrary = true`

위와 마찬가지로 min 21에서 불필요한 옵션이다. <br>
[공식문서](https://developer.android.com/studio/write/vector-asset-studio?hl=ko#sloption)에 따르면
Android 5.0 (API 버전 21) 아래에서 Vector 이미지를 사용할 수 있게 해주는 지원 라이브러리이다.
따라서 삭제를 해준다.

3) `dataBinding true`

해당 앱에서는 `viewBinding`을 사용하고 `dataBinding`은 사용하지 않는다.
필요하지 않은 옵션은 삭제해주었다. 

4) `coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.1.5'`

dusugar는 필요한 경우에만 추가한다.
하지만 해당 앱에서는 필요한 부분이 없기 때문에 삭제 해주었다!

[공식문서 참고](https://developer.android.com/studio/write/java8-support) <br>
[Android 호환성 유지에 대한 고찰 ~ 언어편 _pluu](https://developer.android.com/studio/write/java8-support)

5) Optimize 빌드 옵션 추가하기

![Optimize 빌드 옵션 추가하기](arts/step2_obtimize_build_option.png)

## Step3 : move network

기존에는 네트워크 통신부가 `activity`에 위치해 있었다.
이를 `viewModel`로 옮겨준다.

