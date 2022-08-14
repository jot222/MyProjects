const express = require("express");
const Laundry = require("../models/Laundry.js");
const router = express.Router();

let uniqueMachineID = 1;

router.post("/", async (req, res, next) => {
  let numOfMachines = await Laundry.countDocuments({ machineID: uniqueMachineID });

  while (numOfMachines > 0) {
    uniqueMachineID++;
    numOfMachines = await Laundry.countDocuments({ machineID: uniqueMachineID });
  }

  const laundryMachine = new Laundry({
    dormID: req.body.dormID,
    machineID: uniqueMachineID,
    claimantEmail: req.body.claimantEmail
  });

  laundryMachine.save();

  return res.status(201).send();
});

router.get("/", async (req, res, next) => {
  const laundryMachines = await Laundry.find({}, { _id: 0 });
  return laundryMachines ? res.send(laundryMachines) : res.status(404).send();
});

router.get('/:machine_id', async (req, res, next) => {
  const machine = await Laundry.find({ machineID: req.params.machine_id }, { _id: 0 });
  console.log(machine)
  return machine.length > 0 ? res.send(machine) : res.status(404).send();
});

router.put("/:machine_id", async (req, res, next) => {
  const numOfMachines = await Laundry.countDocuments({ machineID: req.params.machine_id });

  if (numOfMachines > 0) {
    await Laundry.updateOne({ machineID: req.params.machine_id }, { $set: { dormID: req.body.dormID, isClaimed: req.body.isClaimed, machineClaimant: req.body.machineClaimant, inService: req.body.inService, machineTime: req.body.machineTime, claimantEmail:req.body.claimantEmail } });
  } else {
    const laundryMachine = new Laundry({
      dormID: req.body.dormID,
      machineID: req.params.machine_id,
      claimantEmail: req.body.claimantEmail
    });

    laundryMachine.save();

    return res.status(201).send();
  }

  return res.status(200).send();
});

router.delete("/:machine_id", async (req, res, next) => {
  const numOfMachines = await Laundry.countDocuments({ machineID: req.params.machine_id });

  if (numOfMachines > 0) {
    await Laundry.deleteOne({ machineID: req.params.machine_id });
  } else {
    return res.status(404).send();
  }

  return res.status(200).send();
});

module.exports = router;
