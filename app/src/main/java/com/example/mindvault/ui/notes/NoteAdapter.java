package com.example.mindvault.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindvault.R;
import com.example.mindvault.data.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteClick {
        void onClick(Note note);
    }

    private List<Note> notes;
    private final OnNoteClick listener;                 // <— new

    public NoteAdapter(List<Note> notes, OnNoteClick l) {
        this.notes = notes;
        this.listener = l;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup p,int v) {
        View vItem = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_note, p, false);
        return new NoteViewHolder(vItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder h,int pos) {
        Note n = notes.get(pos);
        h.title.setText(n.title);
        h.content.setText(n.content);

        h.itemView.setOnClickListener(v -> listener.onClick(n));   // <— pass back
    }

    @Override public int getItemCount() { return notes==null?0:notes.size(); }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title   = itemView.findViewById(R.id.textNoteTitle);
            content = itemView.findViewById(R.id.textNoteContent);
        }
    }
}
