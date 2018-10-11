package com.liul.trc_study_task.mvp;

import java.lang.ref.WeakReference;

public class BookPresenter{
    private BookModel bookModel;
    private WeakReference<IBookView> weakReference;

    public BookPresenter(){
        this.bookModel=new BookModel();
    }

    public void queryBookInfo(IBookView iBookView){
        weakReference = new WeakReference<IBookView>(iBookView);
        bookModel.queryBookInfo(new OnQueryBookInfoListener(){
            @Override
            public void onSuccess(String bookInfo) {
                weakReference.get().show(bookInfo);
            }
        });
    }

    public void cancelQueryBookInfo(){
        bookModel.cancelQueryBookInfo();
    }

}
