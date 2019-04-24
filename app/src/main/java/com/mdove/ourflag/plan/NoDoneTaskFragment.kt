package com.mdove.ourflag.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.android.base.init.getViewFromPool
import com.mdove.android.base.utils.assemble
import com.mdove.ourflag.R
import com.mdove.ourflag.plan.adapter.NoDoneTaskAdapter
import com.mdove.ourflag.plan.bean.toNormalTaskBean
import com.mdove.ourflag.plan.depends.ITaskPanelDepends
import com.mdove.ourflag.plan.viewmodel.NormalTaskViewModel
import kotlinx.android.synthetic.main.fragment_no_done_task.*

/**
 * Created by MDove on 2019/4/22.
 */
class NoDoneTaskFragment : Fragment() {
    private lateinit var mViewModel: NormalTaskViewModel
    private lateinit var adapter: NoDoneTaskAdapter

    companion object {
        const val ADD_TASK_FRAGMENT_TAG = "add_task_fragment_tag"

        fun instance(bundle: Bundle?): NoDoneTaskFragment {
            val fragment = NoDoneTaskFragment()
            fragment.assemble(bundle)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mViewModel = ViewModelProviders.of(it).get(NormalTaskViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getViewFromPool(R.layout.fragment_no_done_task, container, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NoDoneTaskAdapter({ startAddTask() }, { taskItem ->
            mViewModel.completeTask(taskItem.toNormalTaskBean())
        })
        rlv.layoutManager = LinearLayoutManager(activity)
        rlv.adapter = adapter
        mViewModel.normalTaskItemBeans.observe(this, Observer { data ->
            adapter.updateData(data)
        })
//        mViewModel.completeStatus.observe(this, Observer {
//            ToastUtil.toast(it.toast, Toast.LENGTH_SHORT)
//        })
    }

    private fun startAddTask() {
        (activity as? ITaskPanelDepends)?.let {
            it.addTaskPanel()
        }
    }

    private fun popAddTaskFragment() {
        activity?.let {
            it.supportFragmentManager.popBackStack()
        }
    }
}