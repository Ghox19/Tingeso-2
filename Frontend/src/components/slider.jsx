import React from "react";

function Slider({label, min, max, value, onChange}) {
  const roundToOneDecimal = (num) => Math.round(num * 10) / 10;

  const handleSliderChange = (e) => {
    onChange({
      target: {
        name: 'interest',
        value: Number(e.target.value)
      }
    });
  };

  return (
    <div>
      <input
        type="range"
        min={min}
        max={max}
        value={value}
        step={0.001}
        onChange={handleSliderChange}
        style={{ width: '300px' }}
      />
    </div>
  );
}

export default Slider;