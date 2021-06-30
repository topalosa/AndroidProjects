package com.tp.manage.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tp.manage.activities.*
import com.tp.manage.models.Board
import com.tp.manage.models.User
import com.tp.manage.utils.Constants
import kotlinx.android.synthetic.main.dialog_progress.*

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.userRegisteredSuccess()

                }.addOnFailureListener{
                    e->
                    Log.e(activity.javaClass.simpleName, "Error writing document")
                }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId : String) {
        mFireStore.collection(Constants.BOARD)
                .document(documentId)
                .get()
                .addOnSuccessListener {
                    document ->
                    Log.i(activity.javaClass.simpleName, document.toString())
                    val board = document.toObject(Board::class.java)!!
                    board.documentId = document.id
                    activity.boardDetails(board)

                }.addOnFailureListener { e ->

                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARD)
                .document()
                .set(board, SetOptions.merge())
                .addOnSuccessListener {
                    Log.e(activity.javaClass.simpleName, "Board created successfully")
                    Toast.makeText(activity, "Board created successfully.", Toast.LENGTH_SHORT)
                    activity.boardCreatedSuccessfully()
                }.addOnFailureListener{
                    exception ->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Board created successfully",
                            exception
                    )
        }
    }

    fun getBoardsList(activity: MainActivity){
        mFireStore.collection(Constants.BOARD)
                .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
                .get()
                .addOnSuccessListener {
                    document ->
                    Log.i(activity.javaClass.simpleName, document.documents.toString())
                    val boardList: ArrayList<Board> = ArrayList()
                    for(i in document.documents){
                        val board = i.toObject(Board::class.java)!!
                        board.documentId = i.id
                        boardList.add(board)
                    }

                    activity.populateBoardsListTuUI((boardList))
                }.addOnFailureListener { e ->

                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }
    }

    fun addUpdateTaskList(activity: Activity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARD)
                .document(board.documentId)
                .update(taskListHashMap)
                .addOnSuccessListener {
                    Log.e(activity.javaClass.simpleName, "TaskList updated successfully.")
                    if(activity is TaskListActivity)
                        activity.addUpdateTaskListSuccess()
                    else if (activity is CardDetailsActivity)
                        activity.addUpdateTaskListSuccess()
                }.addOnFailureListener {
                    exception ->
                    if(activity is TaskListActivity)
                        activity.hideProgressDialog()
                    else if (activity is CardDetailsActivity)
                        activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
                }
    }

    fun updateUserProfileData(activity: Activity,
                              userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .update(userHashMap)
                .addOnSuccessListener {
                    Log.i(activity.javaClass.simpleName, "Profile data updated")
                    Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT)
                    when(activity){
                        is MainActivity ->{
                            activity.tokenUpdateSuccess()
                        }
                        is MyProfileActivity ->{
                            activity.profileUpdateSuccess()
                        }
                    }

                }.addOnFailureListener{
                    e ->
                    when(activity){
                        is MainActivity ->{
                            activity.hideProgressDialog()
                        }
                        is MyProfileActivity ->{
                            activity.hideProgressDialog()
                        }
                    }

                    Log.e(activity.javaClass.simpleName,
                    "Error while creating a board.",
                    e)
                }
                Toast.makeText(activity, "Error when updating the profile", Toast.LENGTH_SHORT)
    }

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    val loggedInUser = document.toObject(User::class.java)!!

                    when(activity){
                        is SignInActivity ->{
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity ->{
                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                        }
                        is MyProfileActivity ->{
                            activity.setUserDataInUi(loggedInUser)
                        }
                    }



                }.addOnFailureListener{

                    e->

                when(activity){
                    is SignInActivity ->{
                        activity.hideProgressDialog()
                    }
                    is MainActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                    Log.e("SignInUser", "Error writing document", e)
                }

    }

    fun getCurrentUserId(): String{

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getAssignedMembersListDetails(
            activity: Activity, assignedTo: ArrayList<String>){
            mFireStore.collection(Constants.USERS)
                    .whereIn(Constants.ID, assignedTo)
                    .get()
                    .addOnSuccessListener {
                        document->
                        Log.e(activity.javaClass.simpleName, document.toString())

                        val usersList : ArrayList<User> = ArrayList()

                        for(i in document.documents){
                            val user = i.toObject(User::class.java)!!
                            usersList.add(user)
                        }
                        if(activity is MembersActivity)
                            activity.setupMembersList(usersList)
                        else if(activity is TaskListActivity)
                            activity.boardMembersDetailsList(usersList)
                    }.addOnFailureListener { e->
                        if(activity is MembersActivity)
                            activity.hideProgressDialog()
                        else if(activity is TaskListActivity)
                            activity.hideProgressDialog()
                        Log.e(
                                activity.javaClass.simpleName,
                                "Error while creating a board.",
                                e
                        )
                    }
    }

    fun getMemberDetails(activity: MembersActivity, email: String){
        mFireStore.collection(Constants.USERS)
                .whereEqualTo(Constants.EMAIL, email)
                .get()
                .addOnSuccessListener {
                    document ->
                    if(document.documents.size > 0){
                        val user = document.documents[0].toObject(User::class.java)!!
                        activity.memberDetails(user)
                    }else{
                        activity.hideProgressDialog()
                        activity.showErrorSnackBar("No such member found.")
                    }

                }.addOnFailureListener {  e ->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting user details",
                            e
                    )
                }

    }

    fun assignMemberToBoard(
            activity: MembersActivity, board: Board, user: User){

        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARD)
                .document(board.documentId)
                .update(assignedToHashMap)
                .addOnSuccessListener {
                    activity.memberAssignSuccess(user)
                }
                .addOnFailureListener { e->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }
    }
}