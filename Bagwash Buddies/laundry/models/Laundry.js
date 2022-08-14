const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const laundrySchema = new Schema(
    {
        dormID: { type: Number },
        machineID: { type: Number, unique: true },
        isClaimed: { type: Boolean, default: false },
        machineClaimant: { type: String, default: "", trim: true },
        inService: { type: Boolean, default: true },
        machineTime: { type: Date, default: null },
        claimantEmail: { type: String, default: ""}
    },
    { versionKey: false }
);

const Laundry = mongoose.model("Laundry", laundrySchema);

module.exports = Laundry;
