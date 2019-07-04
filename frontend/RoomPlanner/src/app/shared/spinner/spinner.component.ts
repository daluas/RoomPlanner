import { Component, OnInit, Input } from '@angular/core';
import { ThemePalette, ProgressSpinnerMode } from '@angular/material';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent implements OnInit {

  constructor() { }

  @Input() color: ThemePalette;
  @Input() diameter: number;
  @Input() mode: ProgressSpinnerMode;
  @Input() strokeWidth: number;
  @Input() value: number;
  @Input() isLoading: boolean;

  ngOnInit() {
    this.mode = 'indeterminate';
    this.diameter = 30;
  }

}
