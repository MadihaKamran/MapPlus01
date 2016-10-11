package com.directions.route;
//by Haseem Saheed

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Segment implements Parcelable {
    /**
     * Points in this segment. *
     */
    private LatLng start;
    /**
     * Turn instruction to reach next segment. *
     */
    private String instruction;
    /**
     * Length of segment. *
     */
    private int length;
    /**
     * Distance covered. *
     */
    private double distance;
    
    /* Maneuver instructions */
    private String maneuver;

    /**
     * Create an empty segment.
     */

    public Segment() {
    }


    /**
     * Set the turn instruction.
     *
     * @param turn Turn instruction string.
     */

    public void setInstruction(final String turn) {
        this.instruction = turn;
    }

    /**
     * Get the turn instruction to reach next segment.
     *
     * @return a String of the turn instruction.
     */

    public String getInstruction() {
        return instruction;
    }

    /**
     * Add a point to this segment.
     *
     * @param point GeoPoint to add.
     */

    public void setPoint(final LatLng point) {
        start = point;
    }

    /**
     * Get the starting point of this
     * segment.
     *
     * @return a GeoPoint
     */

    public LatLng startPoint() {
        return start;
    }

    /**
     * Creates a segment which is a copy of this one.
     *
     * @return a Segment that is a copy of this one.
     */

    public Segment copy() {
        final Segment copy = new Segment();
        copy.start = start;
        copy.instruction = instruction;
        copy.length = length;
        copy.distance = distance;
        copy.maneuver = maneuver;
        return copy;
    }

    /**
     * @param length the length to set
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    public void setManeuver(String man) {
        maneuver = man;
    }
    
    public String getManeuver() {
        return maneuver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.start, flags);
        dest.writeString(this.instruction);
        dest.writeInt(this.length);
        dest.writeDouble(this.distance);
        dest.writeString(this.maneuver);
    }

    protected Segment(Parcel in) {
        this.start = in.readParcelable(LatLng.class.getClassLoader());
        this.instruction = in.readString();
        this.length = in.readInt();
        this.distance = in.readDouble();
        this.maneuver = in.readString();
    }

    public static final Parcelable.Creator<Segment> CREATOR = new Parcelable.Creator<Segment>() {
        @Override
        public Segment createFromParcel(Parcel source) {
            return new Segment(source);
        }

        @Override
        public Segment[] newArray(int size) {
            return new Segment[size];
        }
    };
}
