import { Controller, Post, Get, Param, Delete } from '@nestjs/common';
import { DocumentService } from './document.service';

@Controller('documents')
export class DocumentController {

  constructor(private readonly service: DocumentService) {}

  @Post('generate/:studentId')
  generate(@Param('studentId') studentId: number) {
    return this.service.generateConvention(studentId);
  }

  @Get('student/:studentId')
  findByStudent(@Param('studentId') studentId: number) {
    return this.service.findAllByStudent(studentId);
  }

  @Get(':id')
  findOne(@Param('id') id: number) {
    return this.service.findOne(+id);
  }

  @Delete(':id')
  remove(@Param('id') id: number) {
    return this.service.delete(+id);
  }
}