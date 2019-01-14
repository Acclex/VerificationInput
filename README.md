# VerificationInput
[![](https://jitpack.io/v/Acclex/VerificationInput.svg)](https://jitpack.io/#Acclex/VerificationInput)
# 支持数量，背景，光标等自定义的验证码输入框
# How to use
    <com.acclex.verificationinput.VerificationCodeInputView
        android:id="@+id/input_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
       
 监听输入完成：
 
         input_1.setOnCompleteListener {
            log(javaClass.toString(), it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        
监听粘贴文本：

        input_1.setPasteListener { data, box ->
            val c = data.getItemAt(0).coerceToText(this)
            if (c.length > box) {
                c.subSequence(0, box)
            }
            Toast.makeText(this, c, Toast.LENGTH_SHORT).show()
            log(javaClass.toString(), c.toString())
        }
        
删除输入框内所有文本：

    /**
     * 清空验证码输入框，并且focus第一个输入框
     */
    public void restNullCode() {
        EditText editText;
        for (int i = mBox - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            editText.setText("");
        }
        getChildAt(0).requestFocus();
    }
    
设置背景，光标，下划线等：

    <com.acclex.verificationinput.VerificationCodeInputView
        android:id="@+id/input_2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/input_1"
        verification:box="6"
        verification:child_height="50dp"
        verification:child_horizontal_padding="8dp"
        verification:child_width="35dp"
        verification:cursor="@drawable/shape_verification_cursor"
        verification:input_type="number"
        verification:text_color="@color/color_47555f"
        verification:text_size="25"
        verification:underline_focus_color="@color/color_47555f"
        verification:underline_height="4dp"
        verification:underline_normal_color="@color/color_b5bec4"
        verification:underline_space="3dp" />
        
如果使用box_bg_focus和box_bg_normal设置背景，那么通过underline_focus_color和underline_normal_color设置的绘制下划线的属性将会失效,具体可看Demo内xml配置

# Gradle dependencies

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Acclex:VerificationInput:v0.9.0'
	}
