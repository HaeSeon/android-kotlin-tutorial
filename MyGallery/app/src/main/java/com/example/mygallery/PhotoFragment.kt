package com.example.mygallery

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*


private const val ARG_URL = "uri"     //class 밖에서 선언 -> 이 파일 내에 어디서든 사용 가능



class PhotoFragment : Fragment() {
    private lateinit var uri :Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Uri>(ARG_URL)?.let {
            uri = it
            Log.d("MainActivity", uri.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {  //view:생성된 뷰, savedInstanceState:상태를 저장하는 객체
        super.onViewCreated(view, savedInstanceState)

        val descriptor = requireContext().contentResolver.openAssetFileDescriptor(uri, "r")
        descriptor?.use{
            val bitmap = BitmapFactory.decodeFileDescriptor(descriptor.fileDescriptor)
            Glide.with(this).load(uri).into(imageView)   //이미지를 로딩하고 into()메서드로 imageView 에 표시
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(uri: Uri) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URL, uri)
                }
            }
    }
}
