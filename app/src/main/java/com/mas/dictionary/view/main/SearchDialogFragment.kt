package com.mas.dictionary.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mas.dictionary.databinding.SearchDialogBinding


class SearchDialogFragment : BottomSheetDialogFragment() {

    private var onSearchClickListener: OnSearchClickListener? = null
    private var vb: SearchDialogBinding? = null

    private val textWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (vb?.searchEditText?.text != null && vb?.searchEditText?.text.toString()
                    .isNotEmpty()
            ) {
                vb?.searchButtonTextview?.isEnabled = true
                vb?.clearTextImageview?.visibility = View.VISIBLE
            } else {
                vb?.searchButtonTextview?.isEnabled = false
                vb?.clearTextImageview?.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {}
    }

    private val onSearchButtonClickListener =
        View.OnClickListener {
            onSearchClickListener?.onClick(vb?.searchEditText?.text.toString())
            dismiss()
        }

    internal fun setOnSearchClickListener(listener: OnSearchClickListener) {
        onSearchClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return SearchDialogBinding.inflate(inflater, container, false)
            .also<@NonNull SearchDialogBinding> {
                vb = it
            }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb?.searchButtonTextview?.setOnClickListener(onSearchButtonClickListener)
        vb?.searchEditText?.addTextChangedListener(textWatcher)
        addOnClearClickListener()
    }

    override fun onDestroyView() {
        onSearchClickListener = null
        super.onDestroyView()
    }

    private fun addOnClearClickListener() {
        vb?.clearTextImageview?.setOnClickListener {
            vb?.searchEditText?.setText("")
            vb?.searchButtonTextview?.isEnabled = false
        }
    }

    interface OnSearchClickListener {
        fun onClick(searchWord: String)
    }

    companion object {
        fun newInstance(): SearchDialogFragment {
            return SearchDialogFragment()
        }
    }
}
